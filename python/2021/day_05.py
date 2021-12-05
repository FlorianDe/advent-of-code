from collections import defaultdict

from utils.math_utils import Point2D, Line2D


def count_vent_overlaps(lines: list[Line2D], count_diagonals: bool = False) -> int:
    vents: dict[Point2D, int] = defaultdict(lambda: 0)

    for line in lines:
        d_x = line.target.x - line.origin.x
        d_y = line.target.y - line.origin.y
        l1 = max(abs(d_x), (abs(d_y)))
        d_x_norm = int(d_x / l1)
        d_y_norm = int(d_y / l1)
        if d_x != 0 and d_y != 0:
            if count_diagonals and abs(d_x_norm) == 1 and abs(d_y_norm) == 1:
                pass
            else:
                continue

        step_dir = Point2D(d_x_norm, d_y_norm)
        for p in line.origin.step(step_dir):
            vents[p] += 1
            if p == line.target:
                break

    return len([1 for count in vents.values() if count > 1])


if __name__ == '__main__':
    lines = [Line2D.from_tuple(tuple(map(lambda p: Point2D.from_tuple(p.split(",")), line.strip().split(" -> ")))) for
             line in open("inputs/day_05.txt", "r").readlines()]

    print(f"Part01: {count_vent_overlaps(lines)}")
    print(f"Part02: {count_vent_overlaps(lines, count_diagonals=True)}")
