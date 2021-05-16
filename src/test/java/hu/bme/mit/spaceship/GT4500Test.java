package hu.bme.mit.spaceship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class GT4500Test {

    private GT4500 ship;
    private TorpedoStore primary;
    private TorpedoStore secondary;

    @BeforeEach
    public void init() {
        primary = mock(TorpedoStore.class);
        secondary = mock(TorpedoStore.class);

        this.ship = new GT4500(primary, secondary);
    }

    @Test
    public void fireTorpedo_Single_Success() {
        // Arrange
        when(primary.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(result);

        verify(primary, times(1)).fire(1);
        verify(secondary, times(0)).fire(1);
    }

    @Test
    public void fireTorpedo_All_Success() {
        // Arrange
        when(primary.fire(1)).thenReturn(true);
        when(secondary.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.ALL);

        // Assert
        assertTrue(result);

        verify(primary, times(1)).fire(1);
        verify(secondary, times(1)).fire(1);
    }

    /*
    This is the same test as single success, it's here to show that I planned 5 tests
    @Test
    public void fireTorpedoSingle_Primary_Store_Fired_First(){
        fireTorpedo_Single_Success();
    }*/

    @Test
    public void fireTorpedoSingle_Torpedoes_Are_Fired_Alternating(){
        // Arrange
        when(primary.fire(1)).thenReturn(true);
        when(secondary.fire(1)).thenReturn(true);

        // Act
        boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
        boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(result1);
        assertTrue(result2);

        verify(primary, times(1)).fire(1);
        verify(secondary, times(1)).fire(1); //alternating
    }

    @Test
    public void fireTorpedoSingle_If_Store_Is_Empty_Fire_Other_Store(){
        // Arrange
        when(primary.isEmpty()).thenReturn(true);
        when(secondary.isEmpty()).thenReturn(false);

        when(primary.fire(1)).thenThrow(new IllegalArgumentException());
        when(secondary.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert

        assertTrue(result);

        verify(primary, times(0)).fire(1);
        verify(secondary, times(1)).fire(1);
    }

    @Test
    public void fireTorpedoSingle_If_Error_Doesnt_Fire_Other_Store(){
        when(primary.fire(1)).thenThrow(new IllegalArgumentException());
        when(secondary.fire(1)).thenReturn(true);

        // Act
        boolean result;
        try{
            result = ship.fireTorpedo(FiringMode.SINGLE);
        }catch (IllegalArgumentException exception){
            result = false;
        }

        // Assert
        assertFalse(result);

        verify(primary, times(1)).fire(1);
        verify(secondary, times(0)).fire(1);
    }

    @Test
    public void fireTorpedoAll_Fires_Both_Torpedoes(){
        // Arrange
        when(primary.fire(1)).thenReturn(true);
        when(secondary.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.ALL);

        // Assert
        assertTrue(result);

        verify(primary, times(1)).fire(1);
        verify(secondary, times(1)).fire(1);
    }

    @Test
    public void fireTorpedoAll_Try_Firing_Both_But_One_Is_Empty(){
        // Arrange
        when(primary.isEmpty()).thenReturn(true);
        when(secondary.isEmpty()).thenReturn(false);

        when(primary.fire(1)).thenThrow(new IllegalArgumentException());
        when(secondary.fire(1)).thenReturn(true);

        // Act
        boolean result = ship.fireTorpedo(FiringMode.ALL);

        // Assert
        assertTrue(result);

        verify(primary, times(0)).fire(1);
        verify(secondary, times(1)).fire(1);
    }

    @Test
    public void fireTorpedoSingle_Fire_Twice_Secondary_Is_Empty_First_Fires_Again(){
        when(primary.isEmpty()).thenReturn(false);
        when(secondary.isEmpty()).thenReturn(true);

        when(primary.fire(1)).thenReturn(true);
        when(secondary.fire(1)).thenThrow(new IllegalArgumentException());

        // Act
        boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
        boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(result1);
        assertTrue(result2);

        verify(primary, times(2)).fire(1);
        verify(secondary, times(0)).fire(1);
    }

}
