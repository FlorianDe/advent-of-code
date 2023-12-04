import {AocDay, AocDayDecorator, Sets, SolutionParts, Aggregates} from "../aoc";

@AocDayDecorator('2023', '4')
export class Day04 extends AocDay {

	solveImpl(): SolutionParts {
		type ScratchCard = {winningNumbers: Set<number>, revealedNumbers: Set<number>}

		const stringNumbersToIntArray = (row: string) => row.trim()
			.split(/\s+/)
			.map(n => n.trim())
			.map(n => parseInt(n, 10));

		const scratchcards = this.input
			.split("\n")
			.reduce<ScratchCard[]>((acc, row) => {
				const [winNumbers, revealedNumbers] = row.substring(row.indexOf(":")+1)
					.split("|")
					.map(stringNumbersToIntArray)
					.map(nums => new Set(nums));

				acc.push({winningNumbers: winNumbers, revealedNumbers});
				return acc;
			}, []);

		const calculateHits = ({winningNumbers, revealedNumbers}: ScratchCard) =>  Sets.intersection(winningNumbers, revealedNumbers).size;
		const calculateScratchCardWin = (scratchCard: ScratchCard) => {
			const hits = calculateHits(scratchCard);
			return hits > 0 ? 2**(hits-1) : 0;
		};

		const sumOfScratchCardPoints = () => scratchcards.map(calculateScratchCardWin).reduce(Aggregates.sum);

		const amountOfDynamicScratchCards = (): number => {
			const cardAmounts: number[] = Array.from<number>({length: scratchcards.length}).fill(1);
			for (let i = 0; i < cardAmounts.length; i++) {
				const hits = calculateHits(scratchcards[i]);
				for (let step = 1; step <= hits && (i+step <cardAmounts.length); step++) {
					cardAmounts[i+step] += cardAmounts[i];
				}
			}
			return cardAmounts.reduce(Aggregates.sum);
		};

		return {
			part1: sumOfScratchCardPoints(),
			part2: amountOfDynamicScratchCards(),
		};
	}
}