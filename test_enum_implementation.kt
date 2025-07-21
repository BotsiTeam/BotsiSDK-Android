import com.botsi.view.handler.BotsiActionType
import com.botsi.view.handler.BotsiPublicClickHandler

fun main() {
    // Test that all enum values are accessible
    println("Testing BotsiActionType enum values:")
    println("None: ${BotsiActionType.None}")
    println("Close: ${BotsiActionType.Close}")
    println("Login: ${BotsiActionType.Login}")
    println("Restore: ${BotsiActionType.Restore}")
    println("Custom: ${BotsiActionType.Custom}")
    println("Link: ${BotsiActionType.Link}")
    
    // Test that the interface can be implemented with enum types
    val testHandler = object : BotsiPublicClickHandler {
        override fun onButtonClick(actionType: BotsiActionType, actionId: String?, url: String?) {
            println("Button clicked: $actionType, actionId: $actionId, url: $url")
        }
        
        override fun onTopButtonClick(actionType: BotsiActionType, actionId: String?) {
            println("Top button clicked: $actionType, actionId: $actionId")
        }
        
        override fun onLinkClick(url: String) {
            println("Link clicked: $url")
        }
        
        override fun onCustomAction(actionId: String, actionLabel: String?) {
            println("Custom action: $actionId, label: $actionLabel")
        }
    }
    
    // Test the handler with different enum values
    println("\nTesting handler implementation:")
    testHandler.onButtonClick(BotsiActionType.Close, null, null)
    testHandler.onButtonClick(BotsiActionType.Login, "login_action", null)
    testHandler.onButtonClick(BotsiActionType.Link, null, "https://example.com")
    testHandler.onTopButtonClick(BotsiActionType.Close, null)
    testHandler.onLinkClick("https://test.com")
    testHandler.onCustomAction("test_action", "Test Label")
    
    println("\nEnum implementation test completed successfully!")
}