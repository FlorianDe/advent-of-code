import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";
import {Maths} from "../aoc/math";

@AocDayDecorator('2023', '8')
export class Day08 extends AocDay {

	solveImpl(): SolutionParts {
		type RLInstruction = "R" | "L"
		type Node = string;
		type DirectionEntry = Record<RLInstruction, Node>;

		const [rightLeftInstructionsRow, directionsStr] = this.input.split("\n\n");

		const rightLeftInstructions = rightLeftInstructionsRow.trim().split("") as RLInstruction[];
		const directionMap: Record<Node, DirectionEntry> = Object.fromEntries(directionsStr.split("\n").map(row => {
			const [start, endsPart] = row.split("=").map(p => p.trim());
			const [left, right] = endsPart.replaceAll(/[()]/g, "").split(",").map(p => p.trim());
			return [start, {'L': left, 'R': right}];
		}));

		const traverseMap = (startNode: Node, isEndNodePredicate: (node: Node) => boolean): number => {
			let node = startNode;
			let turns = 0;
			do{
				node = directionMap[node][rightLeftInstructions[turns%rightLeftInstructions.length]];
				turns++;
			} while (!isEndNodePredicate(node));
			return turns;
		};

		const part1 = () => traverseMap("AAA", (n) => n === "ZZZ");
		const part2 = () =>  Maths.Arithmetics.lcm(
			Object.keys(directionMap)
				.filter(node => node.endsWith("A"))
				.map(startNode => traverseMap(startNode, (n) => n.endsWith("Z")))
		);

		return {
			part1: part1(),
			part2: part2()
		};
	}
}