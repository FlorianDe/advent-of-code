import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

type GuideSymbolsLeft = 'A' | 'B' | 'C'
type GuideSymbolsRight = 'X' | 'Y' | 'Z'
const HandShapes = [ 'Rock',  'Paper', 'Scissors'] as const;
type HandShape = typeof HandShapes[number];
const ShapeToPoints: Record<HandShape, number> = {
	Rock: 1,
	Paper: 2,
	Scissors: 3
};
const PointsToShape: Record<number, HandShape> = Object.fromEntries(HandShapes.map((key) => [ShapeToPoints[key], key]));
type Round = {
	op: HandShape;
	you: HandShape;
}
type RoundOutcome = 'Lost' | 'Draw' | 'Won'
const RoundOutcomePoints: Record<RoundOutcome, number> = {
	Lost: 0,
	Draw: 3,
	Won: 6
};
@AocDayDecorator('2022', '2')
export class Day02 extends AocDay {
	calcRoundOutcome(round: Round): RoundOutcome {
		if(round.op === round.you){
			return 'Draw';
		}
		if (ShapeToPoints[round.op] % 3 === ShapeToPoints[round.you] - 1){
			return 'Won';
		}
		return 'Lost';
	}

	calculateScore(rounds: Round[]): number {
		return rounds.reduce<number>((acc, curRound) => acc+ShapeToPoints[curRound.you]+RoundOutcomePoints[this.calcRoundOutcome(curRound)],0);
	}

	getMisreadGuideRounds(guideLines: string[]): Round[] {
		const GuideHandShapeMap : Record<GuideSymbolsLeft | GuideSymbolsRight, HandShape> = {
			A: 'Rock',
			B: 'Paper',
			C: 'Scissors',
			X: 'Rock',
			Y: 'Paper',
			Z: 'Scissors'
		};
		return guideLines.map<Round>(line => {
			const hands = line.split(" ");
			return {
				op: GuideHandShapeMap[hands[0] as GuideSymbolsLeft],
				you: GuideHandShapeMap[hands[1] as GuideSymbolsRight],
			};
		});
	}
	getRealGuideRounds(guideLines: string[]): Round[] {
		const GuideHandShapeMap : Record<GuideSymbolsLeft, HandShape> = {
			A: 'Rock',
			B: 'Paper',
			C: 'Scissors'
		};
		const GuideOutcomeMap : Record<GuideSymbolsRight, RoundOutcome> = {
			X: 'Lost',
			Y: 'Draw',
			Z: 'Won'
		};
		const calculateHandShapeForOutcome = (op: HandShape, roundOutcome: RoundOutcome): HandShape => {
			let handMove = 0; // Draw
			if(roundOutcome === 'Won'){
				handMove = 1;
			} else if(roundOutcome === 'Lost') {
				handMove = -1;
			}
			return PointsToShape[(ShapeToPoints[op]-1 + handMove + 3) % 3 + 1];
		};
		return guideLines.map<Round>(line => {
			const hands = line.split(" ");
			const opHand = GuideHandShapeMap[hands[0] as GuideSymbolsLeft];
			return {
				op: opHand,
				you: calculateHandShapeForOutcome(opHand, GuideOutcomeMap[hands[1] as GuideSymbolsRight])
			};
		});
	}
	solveImpl(): SolutionParts {
		const guideLines = this.input.split("\n");
		return {
			part1: this.calculateScore(this.getMisreadGuideRounds(guideLines)),
			part2: this.calculateScore(this.getRealGuideRounds(guideLines)),
		};
	}
}