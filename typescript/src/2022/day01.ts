import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

@AocDayDecorator('2022', '1')
export class Day01 extends AocDay {
	private getSortedElfWeights(): number[] {
		return this.input.split("\n\n")
			.map(list =>
				list.split("\n")
					.map(entry => Number.parseInt(entry, 10))
					.reduce((acc, cur) => acc+cur, 0)
			).sort((a, b) => b - a);
	}

	private getNthSum(weights: number[], elves: number): number {
		return [ ...Array(elves).keys() ].map(idx => weights[idx]).reduce((acc, cur) => acc+cur, 0);
	}

	solveImpl(): SolutionParts {
		const weights = this.getSortedElfWeights();

		return {
			part1: this.getNthSum(weights, 1),
			part2: this.getNthSum(weights, 3),
		};
	}
}