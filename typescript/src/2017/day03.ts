import {AocDay, Solutions} from "../aoc";
import {DiagonalGridDirections, Point2D, VerticalAndHorizontalGridDirections} from "../aoc/math/point";
import {HashMap} from "../aoc/collections/hashmap";

export class Day03 extends AocDay {
	constructor() {
		super(2017, 3);
	}

	private calculateManhattanDistance(gridNumber: number): number {
		const sumCellsUntilLayer = (l: number) =>  4*l*(l-1)+1;
		const cellsInLayerPerSide = (l: number) => 2*l+1;
		/** Calculate the layer by cell value via solving the gaussian sum formula times layer growth 8, +1 for the first cell
		 * 		x = 4*l*(l-1)+1
		 *	<=> 0 = l^2-l+(1-x)/4 */
		const layerByCell = (x: number) => Math.ceil(-1/2 + Math.sqrt(1/4 - (1-x)/4));

		const layer = layerByCell(gridNumber);
		const cellsPerSideFit = cellsInLayerPerSide(layer)-1;
		const cellInLayerPosition = gridNumber - sumCellsUntilLayer(layer);
		const distDiff = Math.abs((cellsPerSideFit)/2-cellInLayerPosition%(cellsPerSideFit));
		return layer+distDiff;
	}

	private calculateGridSeries(gridNumber: number): number {
		const walkDirections = VerticalAndHorizontalGridDirections;
		const neighbours = [
			...VerticalAndHorizontalGridDirections,
			...DiagonalGridDirections
		];

		let curDirIdx = 0;
		let curPos = Point2D.zero();
		const grid = new HashMap<Point2D, number>([[curPos, 1]]);
		for (let step = 1; step < Number.MAX_VALUE; step++) {
			for (let t = 0; t < 2; t++) {
				for (let s = 0; s < step; s++) {
					const nextPos = curPos.add(walkDirections[curDirIdx]);
					const gridValue = neighbours.reduce<number>((acc, cur) => acc + (grid.get(nextPos.add(cur)) ?? 0), 0);
					if(gridValue > gridNumber){
						return gridValue;
					}
					grid.set(nextPos, gridValue);
					curPos = nextPos;
				}
				curDirIdx = (curDirIdx + 1) % walkDirections.length;
			}
		}

		return gridNumber;
	}

	solve(): Solutions {
		const gridNumber = Number.parseInt(this.input, 10);

		return {
			part1: this.calculateManhattanDistance(gridNumber),
			part2: this.calculateGridSeries(gridNumber),
		};
	}
}







