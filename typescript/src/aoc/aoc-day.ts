import {performance} from "perf_hooks";
import {Files} from "./utils";
import {Day, Year} from "./types";

export type AocDayConstructor = new (...args: any[]) => IAocDay
export type AocDayConstructorMap = Partial<Record<Day, AocDayConstructor>>;
export type AocYearDaysConstructorMap = Partial<Record<Year, AocDayConstructorMap>>

const aocDayConstructors: AocYearDaysConstructorMap = {};

export function AocDayDecorator(year: Year, day: Day) {
	return <T extends AocDayConstructor>(constructor: T) => {
		if (!aocDayConstructors[year]) {
			aocDayConstructors[year] = {};
		}
		const aocYear = aocDayConstructors[year];
		const enrichedConstructor = class extends constructor {
			_year = year;
			_day = day;
		};
		if (aocYear && !aocYear[day]) {
			aocYear[day] = enrichedConstructor;
		}
		return enrichedConstructor;
	};
}

export function getAocDays(year: Year, day?: Day): Partial<Record<Day, AocDayConstructor>> {
	const aocYear = aocDayConstructors[year];
	if (!aocYear) {
		throw Error(`No solutions found for year ${year}.`);
	}
	if (!day) {
		return aocYear;
	}
	const aocDay = aocYear[day];
	if (!aocDay) {
		throw Error(`No solutions found for year ${year}, day: ${day}.`);
	}
	return {[day]: aocDay};
}


export type SolutionType = string | number;
export type SolutionParts = {
	part1: SolutionType;
	part2: SolutionType;
}
export type SolutionMetaData = {
	year: Year;
	day: Day;
	duration: number;
}
export type Solution = SolutionParts & SolutionMetaData

export interface IAocDay {
	solve(): Solution;
}

export abstract class AocDay implements IAocDay {
	private static instances = new Map<string, string>();

	protected _input: string | undefined;
	get input(): string {
		if (!this._input) {
			throw Error(`The private _input field of the AocDay implementation ${this.constructor.name} was not set.`);
		}
		return this._input;
	}

	protected _sampleInput: string | undefined;
	get sampleInput(): string {
		if (!this._sampleInput) {
			throw Error(`The private _sampleInput field of the AocDay implementation ${this.constructor.name} was not set.`);
		}
		return this._sampleInput;
	}

	private readonly _year: Year | undefined;
	get year(): Year {
		if (!this._year) {
			throw Error(`The private _year field of the AocDay implementation ${this.constructor.name} should be set via the ${AocDayDecorator.name} decorator.`);
		}
		return this._year;
	}

	private readonly _day: Day | undefined;
	get day(): Day {
		if (!this._day) {
			throw Error(`The private _day field of the AocDay implementation ${this.constructor.name} should be set via the ${AocDayDecorator.name} decorator.`);
		}
		return this._day;
	}

	protected abstract solveImpl(): SolutionParts;

	solve(): Solution {
		this._input = Files.readContent(this.year, this.day);
		this._sampleInput = Files.readContent(this.year, this.day, true);
		const aocDayKey = `${this.year}${this.day}`;
		const aocDayValue = `${this.constructor.name}`;
		const instanceValue = AocDay.instances.get(aocDayKey);
		if (instanceValue) {
			if (instanceValue !== aocDayValue) {
				throw Error(`Cannot call solve for instance ${aocDayValue}, another AocDay implementation ${instanceValue} already claimed the implementation for year: ${this._year} day: ${this._day}.`);
			}
		} else {
			AocDay.instances.set(aocDayKey, aocDayValue);
		}

		const start = performance.now();
		const solutionParts = this.solveImpl();
		const duration = performance.now() - start;
		return {
			...solutionParts,
			duration,
			year: this.year,
			day: this.day
		};
	}

	static formatSolution(solution: Solution, decimals?: number): string {
		return `
${solution.year} - Day${String(solution.day).padStart(2, '0')}:
Part1: ${solution.part1}
Part2: ${solution.part2}
Calculation time: ${(solution.duration).toFixed(decimals ?? 3)}ms`;
	}
}



