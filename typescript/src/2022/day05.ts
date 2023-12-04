import {AocDay, AocDayDecorator, Functions, SolutionParts, Filters} from "../aoc";

@AocDayDecorator('2022', '5')
export class Day05 extends AocDay {

	solveImpl(): SolutionParts {
		const INSTRUCTION_EXTRACTION_REGEX = /move (?<amount>\d+) from (?<from>\d) to (?<to>\d)/;
		const [rawCrates, rawInstructions] = this.input.split("\n\n");

		const crateRows = rawCrates.split("\n");
		const stacksCount = (crateRows[crateRows.length - 1].length + 2) / 4;
		const crateStacks = crateRows.slice(0, -1).reverse()
			.reduce<string[][]>((acc, cur) => {
				const cols = cur.padEnd(stacksCount * 4 - 1, " ").split("").filter((e, idx) => ((idx - 1) % 4) === 0);
				for (const [idx, c] of cols.entries()) {
					if (c.trim()) {
						acc[idx].push(c);
					}
				}
				return acc;
			}, Array.from({length: stacksCount}, () => []));

		const instructions = rawInstructions.split("\n").map(l => l.trim())
			.map(s => INSTRUCTION_EXTRACTION_REGEX.exec(s))
			.filter(Filters.notEmpty)
			.map(match => {
				const {amount, from, to} = match.groups!!;
				return {
					amount: parseInt(amount, 10),
					from: parseInt(from, 10) - 1,
					to: parseInt(to, 10) - 1
				};
			});

		const moveCrates = (stacks: string[][], moveTransform: (crates: string[]) => string[]): string => {
			const copiedStacks = stacks.map(stack => ([...stack]));
			for (const instruction of instructions) {
				const crateChunk = copiedStacks[instruction.from].splice(copiedStacks[instruction.from].length - instruction.amount, instruction.amount);
				copiedStacks[instruction.to].push(...moveTransform(crateChunk));
			}
			return copiedStacks.map(c => c.pop()).join("");
		};

		return {
			part1: moveCrates(crateStacks, (chunk) => chunk.reverse()),
			part2: moveCrates(crateStacks, Functions.identity),
		};
	}
}