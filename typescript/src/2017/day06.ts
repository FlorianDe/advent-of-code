import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

@AocDayDecorator('2017', '6')
export class Day06 extends AocDay {
	solveImpl(): SolutionParts {
		const memoryBanks = this.input.split(/\s+/).map(cell => Number.parseInt(cell, 10));

		const seenMemoryConfigs = new Map<string, number>();
		let cycles = 0;
		let curConfig = [...memoryBanks];
		while (!seenMemoryConfigs.has(curConfig.join())) {
			seenMemoryConfigs.set(curConfig.join(), cycles);
			const iterations = Math.max(...curConfig);
			const idxOfMax = curConfig.indexOf(iterations);
			const nextConfig = [...curConfig];
			nextConfig[idxOfMax] = 0;
			for (let i = 1; i <= iterations; i++) {
				nextConfig[(idxOfMax + i) % nextConfig.length]++;
			}
			curConfig = nextConfig;
			cycles++;
		}
		return {
			part1: cycles,
			part2: cycles - seenMemoryConfigs.get(curConfig.join())!!,
		};
	}
}







