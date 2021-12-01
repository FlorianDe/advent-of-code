lines = [int(distance.strip()) for distance in open("inputs/day_01.txt", "r").readlines()]

def count_increased(distances: list[int], window: int = 1, step_size: int = 1) -> int:
    aggregates = [sum(distances[i:i+window]) for i in range(0, len(distances) - (window-1), step_size)]
    return sum(a < b for a,b in zip(aggregates, aggregates[1:]))

if __name__ == '__main__':
    print(f"Part01: {count_increased(lines)}")
    print(f"Part02: {count_increased(lines, 3)}")