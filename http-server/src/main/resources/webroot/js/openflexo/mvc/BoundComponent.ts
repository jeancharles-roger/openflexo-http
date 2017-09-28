import { Component } from "../ui/Component"
import { Api, BindingId, RuntimeBindingId, ChangeEvent } from "../api/Api"

export abstract class BoundComponent  extends Component {

  readonly container : HTMLSpanElement | HTMLDivElement;

  protected visibleRuntimeBinding: RuntimeBindingId<string>|null = null
  protected visibleChangeListener = (event) => this.setVisible(event.value === "true")

  protected enableRuntimeBinding: RuntimeBindingId<string>|null = null
  protected enableChangeListener = (event) => this.setEnable(event.value === "true");

  public abstract setEnable(enable: boolean);

  updateRuntime(runtime: string|null,extensions: Map<string, string> = new Map<string, string>() ):void {
    // TODO
  }
}
