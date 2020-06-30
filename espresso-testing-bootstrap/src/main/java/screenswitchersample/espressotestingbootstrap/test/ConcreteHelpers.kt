package screenswitchersample.espressotestingbootstrap.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import java.lang.reflect.Field

fun <T> findComponent(name: String): T {
    return findWall<T>(name).component
}

fun <T> findWall(name: String): ConcreteWall<T> {
    val wall = Concrete.findWall<ConcreteWall<Any>>(ApplicationProvider.getApplicationContext<Context>())
    val childrenWallsField = ConcreteWall::class.java.getDeclaredField("childrenWalls")
    childrenWallsField.isAccessible = true
    val foundWall = wallWithName(name, childrenWallsField, wall)
    @Suppress("UNCHECKED_CAST")
    return foundWall as? ConcreteWall<T> ?: throw IllegalStateException("Wall not found. $name")
}

private fun wallWithName(name: String, childrenWallsField: Field, wall: ConcreteWall<*>): ConcreteWall<*>? {
    @Suppress("UNCHECKED_CAST")
    val childrenWalls = childrenWallsField.get(wall) as Map<String, ConcreteWall<*>>
    val foundWall = childrenWalls[name]
    if (foundWall != null) {
        return foundWall
    }
    for (childWall in childrenWalls.values) {
        val foundChildWall = wallWithName(name, childrenWallsField, childWall)
        if (foundChildWall != null) {
            return foundChildWall
        }
    }
    return null
}
