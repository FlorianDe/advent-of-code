import {readInput} from "./utils";

export type SolutionType = string | number;

export type Solutions = {
	part1: SolutionType;
	part2: SolutionType;
}

export abstract class AocDay {
	private static instances = new Map<string, string>();

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
		this.input = readInput(year, day);
		const aocDayKey = `${year}${day}`;
		const aocDayValue = `${this.constructor.name}`;
		const instanceValue = AocDay.instances.get(aocDayKey);
		if(instanceValue){
			if(instanceValue !== aocDayValue){
				throw Error(`Cannot create instance for ${aocDayValue}, another AocDay implementation ${instanceValue} already claimed the implementation for year: ${this._year} day: ${this._day}.`);
			}
		} else {
			AocDay.instances.set(aocDayKey, aocDayValue);
		}
	}

	abstract solve(): Solutions;

	public toString = () : string => {
		const solutions = this.solve();
		return `
${this.year} - Day${String(this.day).padStart(2, '0')}:
Part1: ${solutions.part1}
Part2: ${solutions.part2}`;

	};

}

