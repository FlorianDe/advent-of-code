import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";
import {Point2D, VerticalAndHorizontalGridDirections} from "../aoc/math/point";

@AocDayDecorator('2022', '8')
export class Day08 extends AocDay {

	solveImpl(): SolutionParts {
		const trees = this.input.split("\n").map((line) => line.split("").map(t => parseInt(t, 10)));
		const HEIGHT = trees.length;
		const WIDTH = trees[0].length;

		const countVisibleTreesFromTheEdges = ():number => {
			const visibleTrees = new Set<string>();
			const addIfVisibleTree = (priorMaxHeightInLine: number, p: { y: number, x: number }): number => {
				const treePos = new Point2D(p.x, p.y);
				const treeHeight = trees[treePos.y][treePos.x];
				if (priorMaxHeightInLine < treeHeight) {
					const posHashCode = treePos.hashCode();
					if (!visibleTrees.has(posHashCode)) {
						visibleTrees.add(posHashCode);
					}
					return treeHeight;
				}
				return priorMaxHeightInLine;
			};

			for (let y = 0; y < HEIGHT; y++) {
				let priorMaxHeightInLine = -1;
				for (let x = 0; x < WIDTH; x++) {
					priorMaxHeightInLine = addIfVisibleTree(priorMaxHeightInLine, {y, x});
				}
				priorMaxHeightInLine = -1;
				for (let x = WIDTH - 1; x > 0; x--) {
					priorMaxHeightInLine = addIfVisibleTree(priorMaxHeightInLine, {y, x});
				}
			}
			for (let x = 0; x < WIDTH; x++) {
				let priorMaxHeightInLine = -1;
				for (let y = 0; y < HEIGHT; y++) {
					priorMaxHeightInLine = addIfVisibleTree(priorMaxHeightInLine, {y, x});
				}
				priorMaxHeightInLine = -1;
				for (let y = HEIGHT - 1; y > 0; y--) {
					priorMaxHeightInLine = addIfVisibleTree(priorMaxHeightInLine, {y, x});
				}
			}
			return visibleTrees.size;
		};

		const findBestTreeHouseSpotScore = (): number => {
			let bestScore = 0;
			for (let y = 0; y < HEIGHT; y++) {
				for (let x = WIDTH - 1; x > 0; x--) {
					let score = 1;
					for (const direction of VerticalAndHorizontalGridDirections) {
						let directionScore = 0;
						let curPosition = new Point2D(x, y);
						const treeHouseHeight = trees[y][x];
						while(0 < curPosition.x && curPosition.x < WIDTH-1 && 0 < curPosition.y && curPosition.y < HEIGHT-1){
							curPosition = curPosition.add(direction);
							directionScore++;
							const treeHeight = trees[curPosition.y][curPosition.x];
							if(treeHeight >= treeHouseHeight){
								break;
							}
						}
						score *= directionScore;
					}
					if(score > bestScore){
						bestScore = score;
					}
				}
			}
			return bestScore;
		};

		return {
			part1: countVisibleTreesFromTheEdges(),
			part2: findBestTreeHouseSpotScore(),
		};
	}
}