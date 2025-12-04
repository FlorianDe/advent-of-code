export namespace Maths {
  export function findMax(nums: number[], startIdx: number = 0, endIdx: number = nums.length): { idx: number; value: number } {
    if (startIdx < 0 || startIdx > nums.length) {
      throw new RangeError("'startIdx' is out of bounds.");
    }
    if (endIdx < 0 || endIdx > nums.length) {
      throw new RangeError("'endIdx' is out of bounds.");
    }
    if (startIdx >= endIdx) {
      throw new RangeError("'startIdx' must be less than 'endIdx'.");
    }

    let maxIdx = -1;
    let maxValue = Number.NEGATIVE_INFINITY;

    for (let i = startIdx; i < endIdx; i++) {
      const v = nums[i];
      if (typeof v !== 'number') {
        throw new TypeError(`Element at index ${i} is not a number.`);
      }
      if (v > maxValue) {
        maxValue = v;
        maxIdx = i;
      }
    }

    return { idx: maxIdx, value: maxValue };
  }

  export namespace Arithmetics {
    export function gcd(a: number, b: number): number {
      let aCopy = a;
      let bCopy = b;
      while (bCopy !== 0) {
        const temp = bCopy;
        bCopy = aCopy % bCopy;
        aCopy = temp;
      }
      return aCopy;
    }

    export function lcm(numbers: number[]): number {
      if (numbers.length < 2) {
        throw new Error('At least two numbers are required to find the LCM.');
      }
      const lcmImpl = (a: number, b: number) => (a * b) / gcd(a, b);

      let tempLcm = numbers[0];
      for (let i = 1; i < numbers.length; i++) {
        tempLcm = lcmImpl(tempLcm, numbers[i]);
      }
      return tempLcm;
    }

    export function mod(a: number, n: number): number {
      return ((a % n) + n) % n;
    }
  }
}
