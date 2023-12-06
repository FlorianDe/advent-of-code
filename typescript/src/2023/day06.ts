import {AocDay, AocDayDecorator, Arrays, SolutionParts} from "../aoc";

@AocDayDecorator('2023', '6')
export class Day06 extends AocDay {

	solveImpl(): SolutionParts {

		type BoatGameRecord = { time: number, distance: number }

		const removeSpaces = (input: string) => input.replaceAll(" ", "");
		const parse = (input: string): BoatGameRecord[] => Arrays.transpose(
			input.split("\n")
				.map(row => row.trim().substring(row.indexOf(":")+1).trim())
				.map(row => row.split(/\s+/).map(s => s.trim()).map(s => parseInt(s, 10)))
		).map(([time, distance]) => ({time, distance}));

		const solveQuadratic = ({time, distance}: BoatGameRecord) => {
			// f(t)_m,d = (m-t)*t+d <=> t^2+-1m*t-d --> solve where f(t)=0, m := time, d := distance
			const p = -1*time;
			const q = -distance;
			const p1 = -p/2;
			const p2 = Math.sqrt((p/2)**2 + q);

			return [p1-p2, p1+p2];
		};

		const discreteInnerInterval = ([t1, t2]: number[]) => [Number.isInteger(t1)?t1+1:Math.ceil(t1), Number.isInteger(t2)?t2-1:Math.floor(t2)];
		const intervalPossibilities = ([t1, t2]: number[]) => Math.abs(t1-t2)+1;

		const possibleWinProduct = (games: BoatGameRecord[]): number => games.map(solveQuadratic)
			.map(discreteInnerInterval)
			.map(intervalPossibilities)
			.reduce((acc, cur) => acc*cur, 1);

		return {
			part1: possibleWinProduct(parse(this.input)),
			part2: possibleWinProduct(parse(removeSpaces(this.input))),
		};
	}
}