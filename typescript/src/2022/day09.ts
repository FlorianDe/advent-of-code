import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";
import {Point2D} from "../aoc/math/point";

@AocDayDecorator('2022', '9')
export class Day09 extends AocDay {

	solveImpl(): SolutionParts {
		const directions = {
			"R": new Point2D(1, 0),
			"D": new Point2D(0, 1),
			"L": new Point2D(-1, 0),
			"U": new Point2D(0, -1),
		};

		const moves = this.input.split("\n").map(line => {
			const [direction, amount] = line.split(" ");
			return {direction: direction as 'R' | 'D' | 'L' | 'U', amount: parseInt(amount, 10)};
		});

		const countVisitedCoord = (coordLength: number,): Record<number, number> => {
			const rope = Array.from({length: coordLength}, () => Point2D.zero());
			const visitedCoords = rope.reduce((acc, cur, idx) => {
				acc[idx] = new Set([cur.hashCode()]);
				return acc;
			}, {} as Record<number, Set<string>>);
			for (const move of moves) {
				const dir = directions[move.direction];
				for (let i = 0; i < move.amount; i++) {
					rope[0] = rope[0].add(dir);
					for (let k = 0; k < coordLength - 1; k++) {
						const dist = rope[k].sub(rope[k + 1]);
						const distAbs = dist.abs();
						if (distAbs.y > 1 || distAbs.x > 1) {
							rope[k + 1] = rope[k + 1].add(dist.sign());
						}
						const knotHashCode = rope[k + 1].hashCode();
						if (!visitedCoords[k + 1].has(knotHashCode)) {
							visitedCoords[k + 1].add(knotHashCode);
						}
					}
				}
			}
			return Object.fromEntries(Object.entries(visitedCoords).map(([knot, visited]) => [knot, visited.size]));
		};

		return {
			part1: countVisitedCoord(2)[1],
			part2: countVisitedCoord(10)[9],
		};
	}
}