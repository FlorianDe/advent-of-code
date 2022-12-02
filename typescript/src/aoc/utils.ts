import {readFileSync} from 'fs';
import * as path from 'path';
import {assertValidDay, assertValidYear, Day, Year} from "./types";

export const readInput = (year: number | string | Year, day: number | string | Day): string => {
	assertValidYear(year);
	assertValidDay(day);
	const fileName = `day${String(day).padStart(2, '0')}.txt`;
	const inputTextPath = path.resolve(__dirname, "..", `${year}`, 'inputs', fileName);
	return readFileSync(inputTextPath, {encoding: "utf-8"});
};