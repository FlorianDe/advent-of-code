import {assertValidDay, assertValidYear, Day, Year} from "./types";

type CLIArgs = {
	day?: Day;
	year?: Year;
}

export const parseArgs = (): CLIArgs => {
	const args = process.argv.slice(2);
	const cliArgs:CLIArgs = {};
	if(args){
		if(args.length >= 3){
			throw Error('Currently only one or two parameters are supported <year> <day>');
		}
		if(args.length >= 2) {
			const day = args[1];
			assertValidDay(day);
			cliArgs.day = day;
		}
		if(args.length >= 1){
			const year = args[0];
			assertValidYear(year);
			cliArgs.year = year;
		}
	}
	return cliArgs;
};
