import {readFileSync} from 'fs';
import * as path from 'path';
import {assertValidDay, assertValidYear, Day, Year} from "./types";

export namespace Sets {
	export const intersection = <T>(a: Set<T> | Iterable<T>, b: Set<T>): Set<T> => new Set([...a].filter(x => b.has(x)));
}


export const sum = (acc: number, cur: number): number => acc+cur;

export const chunk = <T>(arr: T[], size: number): T[][] => Array.from({ length: Math.ceil(arr.length / size) }, (v, i) =>
	arr.slice(i * size, i * size + size)
);

export const readInput = (year: number | string | Year, day: number | string | Day): string => {
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