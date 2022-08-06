import {readFileSync} from 'fs';
import * as path from 'path';

const assertValidDay = (day: number): number => {
	if(!Number.isInteger(day) || !(day >= 1 && day <= 25)){
		throw Error("The provided day is not a valid integer between 1 and 25");
	}
	return day;
};

const assertValidYear = (year: number): number => {
	if(!Number.isInteger(year) ||  year < 2015){
		throw Error("The provided day is not a valid integer between 1 and 25");
	}
	return year;
};

export const readInput = (year: number, day: number): string => {
	assertValidYear(year);
	assertValidDay(day);
	const fileName = `day${String(day).padStart(2, '0')}.txt`;
	const inputTextPath = path.resolve(__dirname, "..", `${year}`, 'inputs', fileName);
	return readFileSync(inputTextPath, {encoding: "utf-8"});
};