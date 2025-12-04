import { AocDay, AocDayDecorator, SolutionParts } from '../aoc';
import { Maths } from '../aoc/math';

@AocDayDecorator('2025', '3')
export class Day03 extends AocDay {
  solveImpl(): SolutionParts {
    const batteryPacks = this.input.split('\n').map((row) => row.split('').map((b) => Number.parseInt(b, 10)));

    const findMaximumJoltageSum = (turnOnCount: number) => {
      let sum = 0;
      for (const batteries of batteryPacks) {
        let startIdx = 0;
        let endIdx = batteries.length - turnOnCount + 1;
        for (let i = 0; i < turnOnCount; i++) {
          const max = Maths.findMax(batteries, startIdx, endIdx);
          sum += 10 ** (turnOnCount - (i + 1)) * max.value;
          startIdx = max.idx + 1;
          endIdx++;
        }
      }
      return sum;
    };

    return {
      part1: findMaximumJoltageSum(2),
      part2: findMaximumJoltageSum(12),
    };
  }
}
