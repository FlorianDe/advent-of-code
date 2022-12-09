import {AocDay, AocDayDecorator, SolutionParts} from "../aoc";

@AocDayDecorator('2022', '6')
export class Day06 extends AocDay {

	solveImpl(): SolutionParts {
		const findIdxStartOfPacketMarker = (sequenceLen: number): number => {
			const chars = this.input.trim().split("");
			for (let i = 0; i < chars.length; i++)
				if(new Set(chars.slice(i, i+sequenceLen)).size >= sequenceLen)
					return i + sequenceLen;
			return -1;
		};

		return {
			part1: findIdxStartOfPacketMarker(4),
			part2: findIdxStartOfPacketMarker(14),
		};
	}
}