package ru.resodostudios.muzyakich.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.Before
import org.junit.Test
import ru.resodostudio.muzyakich.core.navigation.NavigationState
import ru.resodostudio.muzyakich.core.navigation.Navigator
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

private object TestStartKey : NavKey
private object TestKey1 : NavKey
private object TestKey2 : NavKey

class NavigatorTest {

    private lateinit var navigationState: NavigationState
    private lateinit var navigator: Navigator

    @Before
    fun setup() {
        val backStack = NavBackStack<NavKey>(TestStartKey)

        navigationState = NavigationState(
            backStack = backStack,
        )
        navigator = Navigator(navigationState)
    }

    @Test
    fun testStartKey() {
        assertEquals(navigationState.startKey, TestStartKey)
    }

    @Test
    fun testNavigate() {
        navigator.navigate(TestKey1)

        assertEquals(navigationState.currentKey, TestKey1)
    }

    @Test
    fun testNavigateSingleTop() {
        navigator.navigate(TestKey1)

        assertContentEquals(
            navigationState.backStack,
            listOf(TestStartKey, TestKey1),
        )

        navigator.navigate(TestKey1)

        assertContentEquals(
            navigationState.backStack,
            listOf(TestStartKey, TestKey1),
        )
    }

    @Test
    fun testGoBack() {
        navigator.navigate(TestKey1)
        navigator.navigate(TestKey2)

        assertContentEquals(
            navigationState.backStack,
            listOf(TestStartKey, TestKey1, TestKey2),
        )

        navigator.goBack()

        assertContentEquals(
            navigationState.backStack,
            listOf(TestStartKey, TestKey1),
        )

        assertEquals(navigationState.currentKey, TestKey1)
    }

    @Test
    fun testGoBackMultiple() {
        navigator.navigate(TestKey1)
        navigator.navigate(TestKey2)

        assertContentEquals(
            navigationState.backStack,
            listOf(TestStartKey, TestKey1, TestKey2),
        )

        navigator.goBack()
        navigator.goBack()

        assertContentEquals(
            navigationState.backStack,
            listOf(TestStartKey),
        )

        assertEquals(navigationState.currentKey, TestStartKey)
    }

    @Test
    fun testGoBackIsNoOpAtStart() {
        assertEquals(navigationState.currentKey, TestStartKey)

        navigator.goBack()

        assertEquals(navigationState.currentKey, TestStartKey)
    }
}
