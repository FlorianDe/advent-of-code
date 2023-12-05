import {AocDay, AocDayDecorator, Arrays, SolutionParts} from "../aoc";
import chunk = Arrays.chunk;

@AocDayDecorator('2023', '5')
export class Day05 extends AocDay {

	solveImpl(): SolutionParts {
		type Mapping = { dest: number, src: number, range: number }

		const [initialSeedRow, ...rest] = this.input.split("\n\n");

		const initialSeeds: number[] = initialSeedRow
			.split(":")[1]
			.trim()
			.split(" ")
			.map(n => Number.parseInt(n.trim(), 10));

		const initialSeedPairs: number[][] = chunk(initialSeeds, 2);

		const mappings = rest
			.map(row => {
					const [name, ...mappingRows] = row.split("\n");

					const mapping = mappingRows.map<Mapping>(mappingRow => {
						const [dest, src, range] = mappingRow.split(" ")
							.map(v => v.trim())
							.map(v => parseInt(v, 10));
						return {
							dest, src, range
						};
					});

					return {
						name,
						mapping
					};
				}
			);

		const transformViaMap = (sMapping: Mapping[], value: number): number => {
			for (const mapping of sMapping) {
				if (mapping.src <= value && value <= mapping.src + mapping.range) {
					return mapping.dest + (value - mapping.src);
				}
			}
			return value;
		};

		const transformToLocation = (initialValue: number) => Object.values(mappings).reduce((value, cur) => transformViaMap(cur.mapping, value), initialValue);

		const part1 = () => Math.min(...initialSeeds.map(transformToLocation));
		const part2 = () => {
			// brute force solution - takes ~7min on MBA M2
			let minLocation = Number.MAX_VALUE;
			for (const [seedStart, range] of initialSeedPairs) {
				console.log({seedStart});
				for (let seed = seedStart; seed < seedStart+range; seed++) {
					const location = transformToLocation(seed);
					if(location < minLocation){
						minLocation = location;
					}
				}
			}
			return minLocation;
		};

		return {
			part1: part1(),
			part2: part2()
		};
	}
}