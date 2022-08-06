import {AocDay, Solutions} from "../aoc";

export class Day01 extends AocDay {
	constructor() {
		super(2017, 1);
	}

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

	solve(): Solutions {
		return {
			part1: this.calcCaptcha((idx, inputLength) => (idx + 1) % inputLength),
			part2: this.calcCaptcha((idx, inputLength) => (idx + (inputLength/2)) % inputLength),
		};
	}
}