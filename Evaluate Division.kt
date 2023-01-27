class Solution {
    fun calcEquation(
            equations: List<List<String>>,
            values: DoubleArray,
            queries: List<List<String>>
    ): DoubleArray {

        val size: Int = equations.size
        val graph: MutableMap<String, ArrayList<Pair<String, Double>>> = mutableMapOf()

        for (idx: Int in 0..size - 1) {
            val u: String = equations[idx][0]
            val v: String = equations[idx][1]
            val value: Double = values[idx]

            graph.putIfAbsent(u, arrayListOf())
            graph.putIfAbsent(v, arrayListOf())

            graph[u]!!.add(Pair(v, value))
            graph[v]!!.add(Pair(u, 1 / value))
        }

        val ans: DoubleArray = DoubleArray(queries.size)
        val num: Int = graph.size

        fun dfs(
                src: String,
                dest: String,
                value: Double,
                done: MutableSet<String>
        ): Pair<Double, Boolean> {
            if (done.contains(src)) return Pair(1.0, false)
            done.add(src)
            for (node: Pair<String, Double> in graph[src]!!) {
                if (done.contains(node.first)) continue
                val newValue: Double = value * node.second
                if (node.first == dest) return Pair(newValue, true)
                val ans: Pair<Double, Boolean> = dfs(node.first, dest, newValue, done)
                if (!ans.second) continue
                return ans
            }

            return Pair(-1.0, false)
        }

        for (idx: Int in 0..queries.size - 1) {
            val u: String = queries[idx][0]
            val v: String = queries[idx][1]

            if (!graph.containsKey(u) || !graph.containsKey(v)) {
                ans[idx] = -1.0
                continue
            }

            if (u == v) {
                ans[idx] = 1.0
                continue
            }

            val done: MutableSet<String> = mutableSetOf()
            val temp1: Pair<Double, Boolean> = dfs(u, v, 1.0, done)
            ans[idx] = if (temp1.second) temp1.first else -1.0
        }

        return ans
    }
}