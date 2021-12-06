from itertools import groupby

from utils.file_utils import get_input


def estimate_fish_population(fish: list[int], days: int = 80, offsprings: list[int] = (7, 9)) -> int:
    fish_groups = {k: len(list(v)) for k, v in groupby(sorted(fish))}
    memory: dict = {}

    def count_siblings(days_left: int, amount: int, days: int):
        if days_left <= days:
            unique_key = (days_left, amount, days)
            if memory.get(unique_key) is None:
                memory[unique_key] = sum(count_siblings(c, amount, days - days_left) for c in offsprings)
            return memory[unique_key]
        return amount

    return sum(count_siblings(days_left + 1, amount, days) for days_left, amount in fish_groups.items())


if __name__ == '__main__':
    lanterns = [int(counter) for counter in get_input().strip().split(",")]

    print(f"Part01: {estimate_fish_population(lanterns)}")
    print(f"Part02: {estimate_fish_population(lanterns, days=256)}")
