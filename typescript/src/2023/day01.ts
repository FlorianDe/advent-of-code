import {AocDay, AocDayDecorator, SolutionParts, sum} from "../aoc";

@AocDayDecorator('2023', '1')
export class Day01 extends AocDay {

	solveImpl(): SolutionParts {
		const numberLiterals = ['zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine'];

		const retrieveInt = (str: string, searchDirection: 'first' | 'last', withLiterals: boolean) => {
			let idx = searchDirection === 'first' ? str.length : -1;
			let num = Number.NaN;
			for (const [valueIdx, value] of [...Object.keys(numberLiterals), ...(withLiterals ? numberLiterals : [])].entries()) {
				const numIdx = searchDirection === 'first' ? str.indexOf(value) : str.lastIndexOf(value);
				if (numIdx !== -1 && (searchDirection === 'first' && numIdx < idx || searchDirection === 'last' && numIdx > idx)) {
					idx = numIdx;
					num = valueIdx % 10;
				}
			}
			return num;
		};

		const getRowNumber = (row: string, withLiterals: boolean) => `${retrieveInt(row, 'first', withLiterals)}${retrieveInt(row, 'last', withLiterals)}`;

		const getCalibrationSum = (withLiterals: boolean) => this.input.split("\n")
			.map((line) => line.trim())
			.map((line) => getRowNumber(line, withLiterals))
			.map(parseInt)
			.reduce(sum);

		return {
			part1: getCalibrationSum(false),
			part2: getCalibrationSum(true)
		};
	}
}