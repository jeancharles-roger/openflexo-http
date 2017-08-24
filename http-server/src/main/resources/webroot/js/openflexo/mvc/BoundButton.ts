import { Api, BindingId, RuntimeBindingId, ChangeEvent } from "../api/Api"
import { Component } from "../ui/Component"
import { PhrasingCategory } from "../ui/category"
import { mdlUpgradeElement } from "../ui/utils"

import { Button } from "../ui/Button"

export class BoundButton implements Component {

    button : Button;

    container: HTMLButtonElement;

    private enabledRuntimeBinding: RuntimeBindingId|null = null
    private enabledChangeListener = (event) => this.listenFromServer(event);

    private actionRuntimeBinding: RuntimeBindingId|null;

    constructor(
        private api: Api, 
        private label: Component|PhrasingCategory,
        private action: BindingId,
        runtime: string|null = null,
        private enabled: BindingId|null = null,
        private type: "raised"|"fab"|"mini-fab"|"icon" = "raised",
        private colored: boolean = false,
        private accent: boolean = false,
        private rippleEffect: boolean = false
     ) {
        this.create();
        this.updateRuntime(runtime);
    }

    create(): void {
        this.button = new Button(this.label, this.type, this.colored, this.accent, this.rippleEffect);
        this.container = this.button.container;
        this.container.onclick = (e) => this.sendActionToServer(e);
    }    

    private listenFromServer(event: ChangeEvent) {
        this.button.setEnable(event.value === "true")
    }

    private sendActionToServer(e: any) {
        if (this.actionRuntimeBinding !== null) {
            this.api.evaluate(this.actionRuntimeBinding).then(value => {
                this.container.classList.remove("mdl-button--colored");
            }).catch(error => {
                this.container.classList.add("mdl-button--colored");
            });
        }
    }

    updateRuntime(runtime: string|null):void {
        if (this.enabledRuntimeBinding !== null) {
            this.api.removeChangeListener(this.enabledRuntimeBinding, this.enabledChangeListener);
        }
        this.enabledRuntimeBinding = null;
        if (runtime !== null) {
            if (this.enabled !== null) {
                this.enabledRuntimeBinding = new RuntimeBindingId(this.enabled, runtime);
                this.api.evaluate<string>(this.enabledRuntimeBinding).then( value => {
                    this.button.setEnable(value === "true")
                } );
                this.api.addChangeListener(this.enabledRuntimeBinding, this.enabledChangeListener);
            }

            this.actionRuntimeBinding = new RuntimeBindingId(this.action, runtime);
        }
    }
}