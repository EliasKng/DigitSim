/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

/**
 *
 * @author Elias
 */
public enum ConnectionType {
    EE,//Element - Element verbindung
    EC,//Element - ConnectionLine Verbingund
    CC,//ConnectionLine - ConnectionLine Verbindung
    E, //Element - ??? Verbindung -> Es steht noch nicht fest, was eine art von Verbindung es sein wird, jedoch aber, dass ein Teil davon ein Element ist (also dass es keine CC verbindung ist)
    C  //ConnectionLine - ???? Verbindung
}
