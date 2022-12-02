import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

type RowAggregator = (row: number[]) => number;

function* crossProductIdxGenerator<T>(arr: T[]): Generator<number[]> {
	for (let a = 0; a < arr.length; a++) {
		for (let b = 0; b < arr.length; b++) {
			yield [a, b];
		}
	}
}

@AocDayDecorator('2017', '2')
export class Day02 extends AocDay {
	solveImpl(): SolutionParts {
		const table = this.input.split("\n")
			.map(row => row.split(/\s+/)
				.map(cell => Number.parseInt(cell, 10))
			);

		const calcTableChecksum = (aggregator: RowAggregator) => table.map(aggregator).reduce((sum, row) => sum + row);
		const maxRowDiff: RowAggregator = (row) => Math.max(...row) - Math.min(...row);
		const evenlyDivisibleValues: RowAggregator = (row) => {
			for (const [a,b] of crossProductIdxGenerator(row)){
				if (a !== b && row[a] % row[b] === 0){
					return row[a] / row[b];
				}
			}
			return 1;
		};
		return {
			part1: calcTableChecksum(maxRowDiff),
			part2: calcTableChecksum(evenlyDivisibleValues),
		};
	}
}