import { performance } from 'perf_hooks';
// import * as y2017 from './2017';
import * as y2022 from './2022';



Object.values(y2022).forEach(AocDay => {
	const start = performance.now();
	console.log(`${new AocDay()}`);
	const end = performance.now();
	console.log(`Calculation time: ${(end-start).toFixed(3)}ms`);
});
