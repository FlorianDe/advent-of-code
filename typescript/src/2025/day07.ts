import { AocDay, AocDayDecorator, SolutionParts } from '../aoc';
import { HashMap } from '../aoc/collections/hashmap';
import { Point2D } from '../aoc/math';

@AocDayDecorator('2025', '7')
export class Day07 extends AocDay {
  solveImpl(): SolutionParts {
    const tachyonManifold = this.sampleInput.split('\n').map((row) => row.split(''));
    const positions = [new Point2D(tachyonManifold[0].indexOf('S'), 0)];
    const downward = new Point2D(0, 1);

    const alreadyCheckedPositions = new HashMap<Point2D, number>();
    let splits = 0;
    while (positions.length > 0) {
      let pos = positions.pop();
      if (pos) {
        while (pos.y < tachyonManifold.length) {
          if (tachyonManifold[pos.y][pos.x] === '^') {
            const newRightAndLeftPos = [new Point2D(pos.x - 1, pos.y), new Point2D(pos.x + 1, pos.y)];
            let split = false;
            for (const newPos of newRightAndLeftPos) {
              if (0 <= newPos.x && newPos.x < tachyonManifold[newPos.y].length && !alreadyCheckedPositions.has(newPos)) {
                split = true;
                positions.push(newPos);
                alreadyCheckedPositions.set(newPos, 1);
              }
            }
            if (split) {
              splits++;
            }
            break;
          }
          // tachyonManifold[pos.y][pos.x] = '|';
          // console.log(tachyonManifold.map((row) => row.join('')).join('\n'));
          pos = pos.add(downward);
        }
      }
    }

    return {
      part1: splits,
      part2: 0, //TODO: FlorianDe: Instead of DFs do Row wise, count beam with count (intensitiy) and sum last row
    };
  }
}
