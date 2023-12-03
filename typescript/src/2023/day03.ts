import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";
import {DiagonalGridDirections, Point2D, VerticalAndHorizontalGridDirections} from "../aoc/math/point";

@AocDayDecorator('2023', '3')
export class Day03 extends AocDay {

	solveImpl(): SolutionParts {

		const schematic2D: string[][] = this.input.split("\n").map(row => row.split(""));

		const isInt = (c: string) => Number.isInteger(Number(c));
		const isSymbol = (c: string) => c !== '.' && !isInt(c);
		const isInsideSchematic = (pos: Point2D) => pos.y >= 0 && pos.y < schematic2D.length && pos.x >= 0 && pos.x < schematic2D[0].length;

		const adjacentNeighbourSteps = [
			...VerticalAndHorizontalGridDirections,
			...DiagonalGridDirections
		];

		let score1 = 0;
		for (let y = 0; y < schematic2D.length; y++) {
			for (let x = 0; x < schematic2D[y].length; x++) {
				let numStr = "";
				let hasSymbolAround = false;
				while (isInt(schematic2D[y][x]) && x < schematic2D[y].length) {
					numStr += schematic2D[y][x];
					if (!hasSymbolAround) {
						const curPos = new Point2D(x, y);
						for (const dir of adjacentNeighbourSteps) {
							const pos = curPos.add(dir);
							if (isInsideSchematic(pos) && isSymbol(schematic2D[pos.y][pos.x])) {
								hasSymbolAround = true;
							}
						}
					}
					x++;
				}
				if (hasSymbolAround) {
					score1 += Number(numStr);
				}
			}
		}

		const extractNumber = (pos: Point2D): { positions: Point2D[], value: number } => {
			const positions = [pos.copy()];
			const walkDirection = (dir: Point2D, appendFn: (acc: string, cur: string) => string): string => {
				let tempNumStr = "";
				let curPos = pos.add(dir);
				while (isInsideSchematic(curPos) && isInt(schematic2D[curPos.y][curPos.x])) {
					positions.push(curPos.copy());
					tempNumStr = appendFn(tempNumStr, schematic2D[curPos.y][curPos.x]);
					curPos = curPos.add(dir);
				}
				return tempNumStr;
			};
			const left = walkDirection(new Point2D(-1, 0), (acc, cur) => cur + acc);
			const right = walkDirection(new Point2D(1, 0), (acc, cur) => acc + cur);
			return {
				positions,
				value: Number(`${left}${schematic2D[pos.y][pos.x]}${right}`)
			};
		};

		let score2 = 0;
		for (let y = 0; y < schematic2D.length; y++) {
			for (let x = 0; x < schematic2D[y].length; x++) {
				if (schematic2D[y][x] === "*") {
					const curPos = new Point2D(x, y);
					const numberNeighbourPos = [];
					for (const dir of adjacentNeighbourSteps) {
						const pos = curPos.add(dir);
						if (isInsideSchematic(pos) && isInt(schematic2D[pos.y][pos.x])) {
							numberNeighbourPos.push(pos);
						}
					}

					const extractedNeighbourNumbers = numberNeighbourPos.map(extractNumber);
					const distinctExtractedNeighbourNumbers = extractedNeighbourNumbers.reduce<Record<string, number>>((acc, cur) => {
						acc[cur.positions.map(p => p.hashCode()).sort().join()] = cur.value;
						return acc;
					},{});
					if (Object.keys(distinctExtractedNeighbourNumbers).length === 2) {
						score2 += Object.values(distinctExtractedNeighbourNumbers).reduce((acc, cur) => acc * cur, 1);
					}
				}
			}
		}


		return {
			part1: score1,
			part2: score2,
		};
	}
}