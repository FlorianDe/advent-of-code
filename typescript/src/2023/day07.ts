import {Aggregates, AocDay, AocDayDecorator, SolutionParts} from "../aoc";

@AocDayDecorator('2023', '7')
export class Day07 extends AocDay {

	solveImpl(): SolutionParts {
		const CardSymbols = ["A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"] as const;
		// eslint-disable-next-line @typescript-eslint/no-redeclare
		type CardSymbols = typeof CardSymbols[number];

		type CardGameRound = {hand: CardSymbols[], bid: number};

		const cardGameRounds: CardGameRound[] = this.input.split("\n").map(r => r.trim()).map(r => {
			const [handStr, bidStr] = r.split(" ");
			return {hand: handStr.trim().split("") as CardSymbols[], bid: parseInt(bidStr.trim(), 10)};
		});

		const getCardAmount = (round: CardGameRound) => round.hand.reduce<Record<string, number>>((acc, cur) => {
			acc[cur] = (acc[cur] ?? 0) + 1;
			return acc;
		}, {});

		const getHandTypeRank = (round: CardGameRound): number => {

			const hasDistinctValues = (amount: number) => Object.keys(getCardAmount(round)).length === amount;
			const hasNofAKind = (amount: number) => Object.values(getCardAmount(round)).find((v) => v === amount) !== undefined;

			const typeRules: Record<string, () => boolean> = {
				isFiveOfAKind: () => hasDistinctValues(1),
				isFourOfAKind: () => hasDistinctValues(2) && hasNofAKind(4),
				isFullHouse: () => hasDistinctValues(2) && hasNofAKind(3),
				isThreeOfAKind: () => hasDistinctValues(3) && hasNofAKind(3),
				isTwoPair: () => hasDistinctValues(3) && hasNofAKind(2),
				isOnePair: () => hasDistinctValues(4) && hasNofAKind(2),
				isHighCard: () => hasDistinctValues(5)
			};

			return Object.keys(typeRules).length - Object.values(typeRules).findIndex((typeRuleFn) => typeRuleFn()) - 1;
		};

		const getBesPossibleRoundWithJokers = (round: CardGameRound): CardGameRound => {
			let best = {
				round,
				roundType: getHandTypeRank(round)
			};
			for (const cardSymbol of CardSymbols.filter(s => s !== "J")) {
				const curRound: CardGameRound = {
					bid: round.bid,
					hand: round.hand.map(c => c === "J" ? cardSymbol : c)
				};
				const curRoundType = getHandTypeRank(curRound);
				if(best.roundType < curRoundType){
					best = {
						round: curRound,
						roundType: curRoundType
					};
				}
			}

			return best.round;
		};

		function createHandSorter(singleCardRankHighestToLowest: CardSymbols[], withJokerUsage: boolean): (a: CardGameRound, b: CardGameRound) => number {
			const singleCardRank = Object.fromEntries(singleCardRankHighestToLowest.map((v, idx, arr) => [v, arr.length - idx - 1]));
			const byHighestCard = (a: CardGameRound, b: CardGameRound): number => a.hand.map((v, idx) => singleCardRank[b.hand[idx]] - singleCardRank[a.hand[idx]]).find(v => v!==0) ?? 0;
			const byHandyTypeRank = (a: CardGameRound, b: CardGameRound): number => {
				if(withJokerUsage){
					return getHandTypeRank(getBesPossibleRoundWithJokers(b)) - getHandTypeRank(getBesPossibleRoundWithJokers(a));
				}
				return getHandTypeRank(b) - getHandTypeRank(a);
			};

			return (a: CardGameRound, b: CardGameRound) => {
				const typeDiff =  byHandyTypeRank(a, b);
				return typeDiff === 0 ? byHighestCard(a, b) : typeDiff;
			};
		}

		const calculateTotalWinnings = (rounds: CardGameRound[],singleCardRankHighestToLowest: CardSymbols[], withJokerUsage: boolean) => {
			const byHandOrder = createHandSorter(singleCardRankHighestToLowest, withJokerUsage);
			return rounds
				.sort(byHandOrder)
				.map((game, idx, acc) => game.bid * (acc.length - idx))
				.reduce(Aggregates.sum);
		};

		const part1 = () => calculateTotalWinnings(cardGameRounds, ["A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"], false);
		const part2 = () => calculateTotalWinnings(cardGameRounds, ["A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J"], true);

		return {
			part1: part1(),
			part2: part2()
		};
	}
}