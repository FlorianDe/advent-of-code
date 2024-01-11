import {Aggregates, AocDay, AocDayDecorator, Arrays, Iterators, SolutionParts} from "../aoc";

@AocDayDecorator('2023', '9')
export class Day09 extends AocDay {

	solveImpl(): SolutionParts {

		const sandInstabilityValuesHistory: number[][] = this.input.split("\n").map(r => r.trim().split(" ").map(v => parseInt(v, 10)));

		const calculateHistoryDiffs = (history: number[]): number[] => Array.from(Iterators.map(Arrays.windowed(history, 2), values => values[1]-values[0]));

		const allZero = (diffs: number[]) => diffs.every(v => v === 0);
		
		const predictHistoryValue = (history: number[], getDiffValueFn: (diffs: number[]) => number, aggregationFn: (values: number[]) => number) => {
			let diffs: number[] = history;
			const lastDiffValues: number[] = [];
			while(!allZero(diffs)){
				if(diffs && diffs.length > 0){
					lastDiffValues.push(getDiffValueFn(diffs));
					diffs = calculateHistoryDiffs(diffs);
				}
			}
			return aggregationFn(lastDiffValues);
		};

		const predictNextHistoryValue = (history: number[]) => predictHistoryValue(
			history,
			(diffs) => diffs[diffs.length - 1],
			(values) => values.reduce(Aggregates.sum),
		);
		const predictPreviousHistoryValue = (history: number[]) => predictHistoryValue(
			history,
			(diffs) => diffs[0],
			(values) => {
				let acc = 0;
				for(let i = values.length - 1; i >= 0; i--){
					acc = values[i] - acc;
				}
				return acc;
			}
		);

		return {
			part1: sandInstabilityValuesHistory.map(predictNextHistoryValue).reduce(Aggregates.sum),
			part2: sandInstabilityValuesHistory.map(predictPreviousHistoryValue).reduce(Aggregates.sum),
		};
	}
}