from utils.file_utils import get_input_lines
from utils.math_utils import Point2D
from utils.measurements import timed

locations = [[int(pos) for pos in location_row.strip()] for location_row in get_input_lines()]


@timed("Part01:")
def part01(locations: list[list[int]]):
    adjacents: list[Point2D] = [
        Point2D(1, 0),
        Point2D(0, 1),
        Point2D(-1, 0),
        Point2D(0, -1),
    ]
    low_points_sum = 0
    height = len(locations)
    for y, row in enumerate(locations):
        width = len(row)
        for x, pos in enumerate(row):
            valid_neighbours = filter(lambda p: 0 <= p.y < height and 0 <= p.x < width,
                                      [Point2D(x, y) + direction for direction in adjacents])
            is_lowest = all(locations[n.y][n.x] > pos for n in valid_neighbours)
            if is_lowest:
                low_points_sum += (pos + 1)
    return low_points_sum


@timed("Part02:")
def part02():
    pass


if __name__ == '__main__':
    part01(locations)
    part02()
