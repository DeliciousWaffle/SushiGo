package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.testcards.*;
import test.testentities.*;
import test.testgamemanager.*;
import test.testguielements.*;

@RunWith(Suite.class)
@SuiteClasses({

    TestCard.class,
    TestDeck.class,
    TestAI.class,
    TestPlayer.class,
    TestGameManager.class,
    TestButton.class,
    TestGameBoard.class
})

public class AllTest {
    
}
