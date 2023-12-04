import {AocDay, AocDayDecorator, SolutionParts, Aggregates} from "../aoc";

@AocDayDecorator('2022', '7')
export class Day07 extends AocDay {

	solveImpl(): SolutionParts {
		const ROOT_NODE = "/";
		type NodePath = string;
		type Node = { parent: NodePath | undefined, children: NodePath[] | undefined, size: number, isFile: boolean }
		const nodes: Record<NodePath, Node> = {};
		const getAbsolutePath = (hierarchy: string[], child?: string) => (!child ? hierarchy : [...hierarchy, child]).join("/");
		const isDir = (node: Node) => !node.isFile;

		let folderHierarchy: string[] = [];
		for (const cmdWithOutput of this.input.slice(2).split("\n$ ")) {
			const cmdArray = cmdWithOutput.split("\n");
			const [command, ...output] = cmdArray;
			const [cmd, args] = command.split(" ");
			if (cmd === 'cd') {
				if (args === '..') {
					folderHierarchy.pop();
				} else if (args === ROOT_NODE) {
					console.log([cmd, args]);
					folderHierarchy = [ROOT_NODE];
				} else {
					folderHierarchy.push(args.trim());
				}
			} else {
				const elements = output.map(o => o.split(" "));
				for (const [sizeOrDir, name] of elements) {
					if (sizeOrDir !== 'dir') {
						nodes[getAbsolutePath(folderHierarchy, name)] = {
							parent: getAbsolutePath(folderHierarchy), // [folderHierarchy.length-1],
							children: undefined,
							size: parseInt(sizeOrDir, 10),
							isFile: true
						};
					}
				}
				nodes[getAbsolutePath(folderHierarchy)] = {
					parent: folderHierarchy.length > 1 ? getAbsolutePath(folderHierarchy.slice(0, -1)) : undefined,
					children: elements.map(([_, name]) => getAbsolutePath(folderHierarchy, name)),
					size: 0,
					isFile: false
				};
			}
		}

		const getFolderSize = (nodeName: string): number => {
			const node = nodes[nodeName];
			if (!node.children) {
				return node.size;
			}

			const nodeSize = node.children.map((child) => getFolderSize(child)).reduce((acc, size) => acc + size, 0);
			nodes[nodeName].size = nodeSize;
			return nodeSize;
		};

		getFolderSize(ROOT_NODE);

		return {
			part1: Object.values(nodes)
				.filter(isDir)
				.filter(e => e.size <= 100000)
				.map(e => e.size).reduce(Aggregates.sum),

			part2: Object.values(nodes)
				.filter(isDir)
				.sort((a,b) => a.size - b.size)
				.find(e => e.size >= nodes[ROOT_NODE].size - (70_000_000 - 30_000_000))?.size ?? -1,
		};
	}
}