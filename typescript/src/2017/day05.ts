import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

@AocDayDecorator('2017', '5')
export class Day05 extends AocDay {
	solveImpl(): SolutionParts {
		const parsedInstructions = this.input.split("\n").map(n => Number.parseInt(n, 10));

		const executeJumps = (isPart2?: boolean): number => {
			const instructions = [...parsedInstructions];
			let idx = 0, steps = 0;
			for (; 0 <= idx && idx < instructions.length; steps++) {
				idx += isPart2 && instructions[idx] >= 3 ? instructions[idx]-- : instructions[idx]++;
			}
			return steps;
		};

		return {
			part1: executeJumps(),
			part2: executeJumps(true),
		};
	}
}







