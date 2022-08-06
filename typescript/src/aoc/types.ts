import {readInput} from "./utils";

export type SolutionType = string | number;

export type Solutions = {
	part1: SolutionType;
	part2: SolutionType;
}

export abstract class AocDay {

	protected input: string;
	private readonly _year: number;
	get year(): number {
		return this._year;
	}

	private readonly _day: number;
	get day(): number {
		return this._day;
	}

	protected constructor(year: number, day: number) {
		this._year = year;
		this._day = day;
		this.input = readInput(year, day)
	}

	abstract solve(): Solutions;

	public toString = () : string => {
		const solutions = this.solve();
		return `
${this.year} - Day${String(this.day).padStart(2, '0')}:
Part1: ${solutions.part1}
Part2: ${solutions.part2}`;
	}

}

