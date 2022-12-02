import {AocDay, SolutionParts} from "../aoc";

export class Day02 extends AocDay {
	solveImpl(): SolutionParts {
		type SymbolMapFn = ([ops, you]: number[]) => number[];
		const SYMBOL_MAP = Object.fromEntries(['A', 'B', 'C', 'X', 'Y', 'Z'].map((e, idx) => [e, idx % 3]));
		const symbolsToNumber = ([ops, you]: string[]): number[] => [SYMBOL_MAP[ops], SYMBOL_MAP[you]];
		const remEuclid = (n: number, mod: number) => ((n % mod) + mod) % mod;
		const sum = (acc: number, cur: number): number => acc+cur;
		const roundScore = ([ops, you]: number[]): number => (2 - remEuclid(ops - you + 1, 3)) * 3 + you+1;
		const identity: SymbolMapFn = (symbols) => symbols;
		const outcomeToSymbol: SymbolMapFn = ([ops, you]) => [ops, remEuclid(ops + (you-1), 3)];
		const totalScore = (rounds: number[][], guideTranslation: SymbolMapFn): number => rounds
			.map(guideTranslation)
			.map(roundScore)
			.reduce(sum);

		const rounds = this.input.split('\n')
			.map(l => l.trim().split(' '))
			.map(symbolsToNumber);

		return {
			part1: totalScore(rounds, identity),
			part2: totalScore(rounds, outcomeToSymbol),
		};
	}
}

console.log(new Day02().solve());