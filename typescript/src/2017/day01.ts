import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

@AocDayDecorator('2017', '1')
export class Day01 extends AocDay {
	private calcCaptcha(nextDigitIndexSupplier: (idx: number, inputLength: number) => number): number {
		const inputLength = this.input.split("").length;
		let sum = 0;
		for (let i = 0; i < inputLength; i++) {
			if (this.input[i] === this.input[nextDigitIndexSupplier(i, inputLength)]) {
				sum += Number.parseInt(this.input[i], 10);
			}
		}
		return sum;
	}

	solveImpl(): SolutionParts {
		return {
			part1: this.calcCaptcha((idx, inputLength) => (idx + 1) % inputLength),
			part2: this.calcCaptcha((idx, inputLength) => (idx + (inputLength/2)) % inputLength),
		};
	}
}