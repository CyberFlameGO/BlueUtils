package net.axay.blueutils.geometry

data class Circle(val radius: Int, val center: Location2D) {

    val fillLocations: Set<Location2D> by lazy {

        val locationList: MutableSet<Location2D> = HashSet()

        val xLoc: Int = center.x
        val yLoc: Int = center.y

        var currentRadius: Int = radius
        while (currentRadius >= 0) {

            var d: Int = -currentRadius
            var x: Int = currentRadius
            var y = 0

            while (y <= x) {

                locationList.addCircleLoc(x, y)
                locationList.addCircleLoc(x, -y)
                locationList.addCircleLoc(-x, y)
                locationList.addCircleLoc(-x, -y)
                locationList.addCircleLoc(y, x)
                locationList.addCircleLoc(y, -x)
                locationList.addCircleLoc(-y, x)
                locationList.addCircleLoc(-y, -x)

                d += 2 * y + 1
                y++

                if (d > 0) {
                    d += -2 * 3 + 2
                    x--
                }

            }

            currentRadius--

        }

        return@lazy locationList

    }

    private fun MutableSet<Location2D>.addCircleLoc(first: Int, second: Int) {
        this += Location2D(center.x + first, center.y + second)
    }

}