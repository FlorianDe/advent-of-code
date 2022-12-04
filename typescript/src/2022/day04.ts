import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

@AocDayDecorator('2022', '4')
export class Day04 extends AocDay {

	solveImpl(): SolutionParts {
		const rows = this.input.split("\n").map(l => {
				const [first, second] = l.split(",").map(i => {
						const [from, to] = i.split("-").map(n => parseInt(n, 10));
						return {from, to};
					}
				);
				return {first, second};
			}
		);

		return {
			part1: rows.filter((r) => (r.first.from <= r.second.from && r.second.to <= r.first.to) || (r.second.from <= r.first.from && r.first.to <= r.second.to)).length,
			part2: rows.filter((r) => (r.first.from <= r.second.from && r.second.from <= r.first.to) || (r.second.from <= r.first.from && r.first.from <= r.second.to)).length,
		};
	}
}