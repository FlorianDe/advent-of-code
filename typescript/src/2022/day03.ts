import {AocDay, AocDayDecorator, chunk, Sets, SolutionParts, sum} from "../aoc";

@AocDayDecorator('2022', '3')
export class Day03 extends AocDay {
	getBadgePriority = (badge: string): number => badge === badge.toUpperCase() ? badge.charCodeAt(0) - 'A'.charCodeAt(0) + 27: badge.charCodeAt(0) - 'a'.charCodeAt(0) + 1;

	getDuplicateItem = (compartments: string[]): string => {
		const [firstCompartment, ...restCompartments] = compartments;
		const duplicates = restCompartments.reduce((intersection, backpack) => Sets.intersection(backpack, intersection), new Set(firstCompartment));
		if(duplicates.size !== 1){
			throw Error(`Not a single unique duplicate found. Duplicate badges of compartments ${compartments} => ${[...duplicates]}`);
		}
		 return [...duplicates][0];
	};

	score = (groupedCompartments: string[][]): number => groupedCompartments
		.map(this.getDuplicateItem)
		.map(this.getBadgePriority)
		.reduce(sum);

	solveImpl(): SolutionParts {
		const lines = this.input.split("\n");
		return {
			part1: this.score(lines.map((line) => [line.substring(0, line.length/2), line.substring(line.length/2, line.length)])),
			part2: this.score(chunk(lines, 3)),
		};
	}
}
