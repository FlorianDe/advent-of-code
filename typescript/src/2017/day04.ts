import {AocDay, Solutions} from "../aoc";

type WordHashFn = (word: string) => string
export class Day04 extends AocDay {
	constructor() {
		super(2017, 4);
	}

	solve(): Solutions {
		const passphrases = this.input.split("\n").map(line => line.split(" "));

		const rowSecurityCheck = (hashFn?: WordHashFn) => passphrases.filter(row => {
			const hashedRow = hashFn ? row.map(hashFn) : row;
			return hashedRow.length === new Set(hashedRow).size;
		}).length;

		const wordSortHashFn: WordHashFn = (word) => word.split("").sort().join("");

		return {
			part1: rowSecurityCheck(),
			part2: rowSecurityCheck(wordSortHashFn),
		};
	}
}







