package view;

public interface ContextPaneQueryChanged {
	
	public void queryChanged(String oldQuery, String newQuery, QueryResultInjectee injectee);

}
