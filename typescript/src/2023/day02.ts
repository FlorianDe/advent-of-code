import {AocDay, AocDayDecorator, SolutionParts, sum} from "../aoc";

@AocDayDecorator('2023', '2')
export class Day02 extends AocDay {

	solveImpl(): SolutionParts {

		type DrawGame = {id: number, draws: Record<string, number>[]};

		const balls: Record<string, number> = {
			red: 12,
			green: 13,
			blue: 14,
		};

		const rows  = this.input.split("\n");
		const games: DrawGame[] = rows.map((row) => {
			const [gameRow, drawRow] = row.trim().split(":");
			const id = parseInt(gameRow.trim().split(" ")[1], 10);
			const draws = drawRow.trim().split(";").map((draw) => {
				const colors = draw.trim().split(",");
				return colors.map<[number, string]>((colorDraw) => {
					const [amount, color] = colorDraw.trim().split(" ");
					return [parseInt(amount, 10), color];
				}).reduce<Record<string, number>>((acc, [amount, color] ) => {
					acc[color] = (acc[color] ?? 0) + amount;
					return acc;
				}, {});
			});

			return {
				id,
				draws
			};
		});

		function hasEnoughBalls(game: DrawGame) {
			for (const draw of Object.values(game.draws)) {
				for (const [color, amount] of Object.entries(draw)) {
					if (balls[color] < amount) {
						return false;
					}
				}
			}
			return true;
		}

		const part1 = games.filter(hasEnoughBalls).map(g => g.id).reduce(sum);
		const part2 = games.reduce<number>((gameSum,game) => {
			const maxOfEachColorPerGame = Object.keys(balls).reduce<Record<string, number>>((acc, color) => {
				game.draws.forEach((draw) => {
					if (!acc[color] || draw[color] > acc[color]) {
						acc[color] = draw[color];
					}
				});
				return acc;
			}, {});
			return gameSum + Object.values(maxOfEachColorPerGame).reduce((acc, amount) => acc * amount, 1);
		}, 0);

		return {
			part1,
			part2
		};
	}
}