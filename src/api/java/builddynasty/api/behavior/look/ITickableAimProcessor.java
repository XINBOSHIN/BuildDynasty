
package builddynasty.api.behavior.look;

import builddynasty.api.utils.Rotation;

public interface ITickableAimProcessor extends IAimProcessor {


    void tick();


    void advance(int ticks);


    Rotation nextRotation(Rotation rotation);
}
