import {AocDay, AocDayConstructor, AocDayConstructorMap, getAocDays, parseArgs, Solution} from "./aoc";

import * as y2017 from './2017';
import * as y2022 from './2022';

const {year, day} = parseArgs();

const runDay = (AocDayCtr: AocDayConstructor): Solution => {
	const solution = new AocDayCtr().solve();
	console.log(`${AocDay.formatSolution(solution)}`);
	return solution;
};

const runYear = (aocDayCtrMap: AocDayConstructorMap | Record<any, AocDayConstructor>) => {
	const aocDayConstructors = Object.values(aocDayCtrMap);
	const totalDuration = aocDayConstructors
		.map(runDay)
		.reduce((acc, cur) => acc+cur.duration,0);
	console.log(`Total time for ${aocDayConstructors.length} days: ${(totalDuration).toFixed(3)}ms`);
};

if(year){
	Object.values(getAocDays(year, day)).forEach(runDay);
} else {
	[y2017, y2022].forEach(runYear);
}



