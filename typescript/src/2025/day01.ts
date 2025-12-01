import { AocDay, AocDayDecorator, SolutionParts } from "../aoc";
import { Maths } from "../aoc/math";

@AocDayDecorator("2025", "1")
export class Day01 extends AocDay {
  solveImpl(): SolutionParts {
    const isAtZero = (params: { pos: number; dialSize: number }) =>
      Maths.Arithmetics.mod(params.pos, params.dialSize) === 0;

    const rotations = this.input
      .replaceAll("R", "")
      .replaceAll("L", "-")
      .split("\n")
      .map((s) => Number.parseInt(s.trim(), 10));

    const processRotations = (
      countFn: (params: {
        pos: number;
        dialSize: number;
        rotation: number;
      }) => number,
      startPos: number = 50,
      dialSize: number = 100
    ) =>
      rotations.reduce(
        (acc, rotation) => {
          acc.count += countFn({ pos: acc.pos, rotation, dialSize });
          acc.pos = Maths.Arithmetics.mod(acc.pos + rotation, dialSize);
          return acc;
        },
        { count: 0, pos: startPos }
      ).count;

    return {
      part1: processRotations(({ pos, rotation, dialSize }) =>
        isAtZero({ pos: pos + rotation, dialSize }) ? 1 : 0
      ),
      part2: processRotations(({ pos, rotation, dialSize }) => {
        if (rotation === 0) return 0;
        const { from, to } =
          rotation > 0
            ? { from: pos + 1, to: pos + rotation }
            : { from: pos + rotation, to: pos - 1 };
        return Math.max(
          0,
          Math.floor(to / dialSize) - Math.ceil(from / dialSize) + 1
        );
      }),
    };
  }
}
