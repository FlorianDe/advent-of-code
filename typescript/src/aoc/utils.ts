import {readFileSync} from 'fs';
import * as path from 'path';
import {assertValidDay, assertValidYear, Day, Year} from "./types";

export namespace Iterators {
	export function* map<T1, T2>(iterable: IterableIterator<T1>, callback: (value: T1) => T2) {
		for (const x of iterable) {
			yield callback(x);
		}
	}
}
export namespace Sets {
	export const intersection = <T>(a: Set<T> | Iterable<T>, b: Set<T>): Set<T> => new Set([...a].filter(x => b.has(x)));
}

export namespace Functions {
	export const identity = <T>(e: T) => e;
}

export namespace Aggregates {
	export const sum = (acc: number, cur: number): number => acc+cur;
}

export namespace Filters {
	export const notEmpty = <T>(value: T | null | undefined): value is T => value !== null && value !== undefined;
}

export namespace Arrays {
	export const transpose = <T>(arr: T[][]): T[][] => arr[0].map((_, colIndex) => arr.map(row => row[colIndex]));
	export const chunk = <T>(arr: T[], size: number): T[][] => Array.from({ length: Math.ceil(arr.length / size) }, (v, i) =>
		arr.slice(i * size, i * size + size)
	);
	export const windowed = <T>(values: T[], windowSize: number, start = 0, end = Infinity, step = 1): IterableIterator<T[]> => {
		let nextIndex = start;
		let iterationCount = 0;

		return {
			[Symbol.iterator](): IterableIterator<T[]> {
				return this;
			},
			next() {
				const endIndex = nextIndex + windowSize;
				if (values && values.length > 0 && endIndex <= end && endIndex <= values.length) {
					iterationCount++;
					const value = values.slice(nextIndex, Math.min(endIndex, values.length));
					nextIndex += step;
					return { value, done: false };
				}
				return { value: iterationCount, done: true };
			}
		};;
	};
}

export namespace Files {
	export const readContent = (year: number | string | Year, day: number | string | Day): string => {
		assertValidYear(year);
		assertValidDay(day);
		const fileName = `day${String(day).padStart(2, '0')}.txt`;
		const inputTextPath = path.resolve(__dirname, "..", `${year}`, 'inputs', fileName);
		const fileContent = readFileSync(inputTextPath, {encoding: "utf-8"});
		if(fileContent.trim().length === 0){
			throw new Error(`The content of the puzzle input for the year ${year} day ${day} is empty!`);
		}
		return fileContent;
	};
}
