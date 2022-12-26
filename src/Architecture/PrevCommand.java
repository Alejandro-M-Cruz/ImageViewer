package Architecture;

public class PrevCommand implements Command {
    private final ImagePresenter presenter;
    
    public PrevCommand(ImagePresenter presenter) {
        this.presenter = presenter;
    }
    
    @Override
    public void execute() {
        presenter.show(presenter.current().prev());
    }
}
